package salmon.community.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import salmon.community.dto.NotificationDTO;
import salmon.community.dto.PaginationDTO;
import salmon.community.enums.NotificationStatusEnum;
import salmon.community.enums.NotificationTypeEnum;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;
import salmon.community.mapper.NotificationMapper;
import salmon.community.mapper.UserMapper;
import salmon.community.model.Notification;
import salmon.community.model.NotificationExample;
import salmon.community.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SalmonC
 * @date 2021-09-07 15:47
 */
@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Long id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);

        //查找总通知数
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        Integer totalPage;
        totalPage = totalCount / size + (totalCount % size == 0 ? 0 : 1);

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setTotalPage(totalPage);

        //查找该页通知
        Integer offset = size * (page - 1);
        notificationExample.setOrderByClause("GMT_CREATE desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));

        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
            boolean isUnread = notificationDTO.getStatus().equals(NotificationStatusEnum.UNREAD.getStatus());
            notificationDTO.setIsUnread(isUnread);
            notificationDTOS.add(notificationDTO);
        }


        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;


    }

    public Long unreadCount(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO readNotification(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException((CustomizeErrorCode.NOTIFICATION_NOT_FOUND));
        }
        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
        return notificationDTO;
    }
}
