package efrei.m1.aiws.model.requests;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class JSONAuthPostRequest {
	private String email = "";

	private String password = "";
}
