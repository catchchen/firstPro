package run.app.model.dto;

import java.util.Date;
import lombok.Data;
import run.app.model.dto.base.OutputConverter;
import run.app.model.entity.Journal;
import run.app.model.enums.JournalType;

/**
 * Journal dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-24
 */
@Data
public class JournalDTO implements OutputConverter<JournalDTO, Journal> {

    private Integer id;

    private String sourceContent;

    private String content;

    private Long likes;

    private Date createTime;

    private JournalType type;
}
