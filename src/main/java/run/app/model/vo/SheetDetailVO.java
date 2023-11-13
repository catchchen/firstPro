package run.app.model.vo;

import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.app.model.dto.BaseMetaDTO;
import run.app.model.dto.post.BasePostDetailDTO;

/**
 * Sheet detail VO.
 *
 * @author ryanwang
 * @date 2019-12-10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetDetailVO extends BasePostDetailDTO {

    private Set<Long> metaIds;

    private List<BaseMetaDTO> metas;
}
