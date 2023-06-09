package ChamStudy.Dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsQnaListDto {
	
	private Long id;
	
	private String title;
	
	private String email;
	
	private String regdate;
	
	private String replyDate;
	
	private String checked;
	
	private Long conId;
	
	@QueryProjection
	public CsQnaListDto(Long id, String title, String email, String regdate, String replyDate, String checked, Long conId) {
		this.id = id;
		this.title = title;
		this.email = email;
		this.regdate = regdate;
		this.replyDate = replyDate;
		this.checked = checked;
		this.conId = conId;
	}
}
