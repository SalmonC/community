package salmon.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import salmon.community.mapper.SoupMapper;
import salmon.community.model.Soup;
import salmon.community.model.SoupExample;
import salmon.community.provider.SoupProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author SalmonC
 * @date 2021-09-10 02:25
 */
@Service
public class SoupService {

    @Autowired
    SoupMapper soupMapper;

    @Autowired
    SoupProvider soupProvider;

    public String getSoup() {
        SoupExample example = new SoupExample();
        Soup soup = soupMapper.selectByPrimaryKey(1);
        if (soup != null) {
            Long gmtUpdate = soup.getGmtUpdate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            String update = dateFormat.format(gmtUpdate);
            String today = dateFormat.format(System.currentTimeMillis());
            if (update.equals(today)) {
                return soup.getContent();
            }
        }
        String newSoup = soupProvider.getSoup();
        if (soup == null) {
            soup = new Soup();
            soup.setId(1);
            soup.setGmtUpdate(System.currentTimeMillis());
            soup.setContent(newSoup);
            soupMapper.insert(soup);
        }
        soup.setGmtUpdate(System.currentTimeMillis());
        soup.setContent(newSoup);
        soupMapper.updateByPrimaryKey(soup);
        return newSoup;
    }
}
