package salmon.community.dto;

import lombok.Data;
import salmon.community.Constants;

import java.util.LinkedList;
import java.util.List;

/**
 * @author SalmonC
 * @date 2021-08-30 23:56
 */
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNextPage;
    private boolean showEndPage;
    private Integer totalPage;
    /**
     * 当前显示的页数
     */
    private Integer page;
    /**
     * 页数栏要显示的页数
     */
    private List<Integer> pages = new LinkedList<>();

    public void setPagination(Integer totalPage, Integer page, Integer size) {
        this.page = page;

        pages.add(page);
        for (int i = 1; i <= Constants.PAGE_WIDTH; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        if (totalPage == 0) {
            return;
        }
        showPrevious = page != 1;
        showNextPage = !page.equals(totalPage);
        showFirstPage = !pages.contains(1);
        showEndPage = !pages.contains(totalPage);
    }
}
