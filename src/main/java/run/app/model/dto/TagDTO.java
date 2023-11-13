package run.app.model.dto;

import java.util.Date;
import lombok.Data;
import run.app.model.dto.base.OutputConverter;
import run.app.model.entity.Tag;
import run.app.model.support.HaloConst;

/**
 * Tag output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Data
public class TagDTO implements OutputConverter<TagDTO, Tag> {

    private Integer id;

    private String name;

    private String slug;

    private String color = HaloConst.DEFAULT_TAG_COLOR;

    private String thumbnail;

    private Date createTime;

    private String fullPath;
}
