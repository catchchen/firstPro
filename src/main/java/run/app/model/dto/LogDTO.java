package run.app.model.dto;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.app.model.dto.base.OutputConverter;
import run.app.model.entity.Log;
import run.app.model.enums.LogType;

/**
 * @author johnniang
 * @date 3/19/19
 */
@Data
@ToString
@EqualsAndHashCode
public class LogDTO implements OutputConverter<LogDTO, Log> {

    private Long id;

    private String logKey;

    private LogType type;

    private String content;

    private String ipAddress;

    private Date createTime;
}
