package efrei.m1.aiws.model.requests;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class JSONUsersPostRequest {
	private String email = "";

	private String password = "";

	private String city = "";
}
