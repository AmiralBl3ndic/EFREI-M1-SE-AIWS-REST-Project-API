package efrei.m1.aiws.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor
public class DVD {
	private static final int DEFAULT_RATING = 0;
	private static final int MINIMUM_RATING = 0;
	private static final int MAXIMUM_RATING = 5;

	private String dvdId;

	private String userId;

	private String ageLimit;

	private String duration;

	private String title;

	private String type;

	private String description;

	private String editor;

	private String audio;

	private String releaseDate;

	@Setter(AccessLevel.NONE)
	private int rating;

	public void setRating(int rating) {
		if(rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING) {
			this.rating = rating;
		}
	}

	public DVD(String dvdId, String userId, String ageLimit, String duration, String title, String type, String description, String editor, String audio, String releaseDate, int rating) {
		this.dvdId = dvdId;
		this.userId = userId;
		this.ageLimit = ageLimit;
		this.duration = duration;
		this.title = title;
		this.type = type;
		this.description = description;
		this.editor = editor;
		this.audio = audio;
		this.releaseDate = releaseDate;
		this.rating = rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING ? rating : DEFAULT_RATING;
	}
}
