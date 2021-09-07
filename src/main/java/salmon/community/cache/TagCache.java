package salmon.community.cache;

import org.apache.commons.lang3.StringUtils;
import salmon.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author SalmonC
 * @date 2021-09-07 01:18
 */
public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO gender = new TagDTO();
        gender.setCategoryName("您的性别");
        gender.setTags(Arrays.asList("男","女","其他","无"));
        tagDTOS.add(gender);
        TagDTO age = new TagDTO();
        age.setCategoryName("您的年龄");
        age.setTags(Arrays.asList("0-5","6-10","11-15","16-18","老了"));
        tagDTOS.add(age);
        TagDTO type = new TagDTO();
        type.setCategoryName("文章类型");
        type.setTags(Arrays.asList("提问","分享","摸鱼"));
        tagDTOS.add(type);
        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        Set<String> tagSet = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toSet());
        String invalid = Arrays.stream(split).filter(t -> !tagSet.contains(t)).collect(Collectors.joining(","));

        return invalid;
    }
}
