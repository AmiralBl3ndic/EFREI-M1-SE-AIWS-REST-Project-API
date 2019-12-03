package efrei.m1.aiws.model.requests;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class UsersResourceResponse {
	private String id = "";

	private String token = "";

	private String error = "";
}
