package efrei.m1.aiws.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor
public class VideoGame {
	private static final int DEFAULT_RATING = 0;
	private static final int MINIMUM_RATING = 0;
	private static final int MAXIMUM_RATING = 5;

	private int videoGameId;

	private int userId;

	private String name;

	private String type;

	private String resume;

	private String editor;

	private String releaseDate;

	@Setter(AccessLevel.NONE)
	private int rating;

	public void setRating(int rating) {
		if(rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING) {
			this.rating = rating;
		}
	}

	public VideoGame(int videoGameId, int userId, String name, String type, String resume, String editor, String releaseDate, int rating) {
		this.videoGameId = videoGameId;
		this.userId = userId;
		this.name = name;
		this.type = type;
		this.resume = resume;
		this.editor = editor;
		this.releaseDate = releaseDate;
		this.rating = rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING ? rating : DEFAULT_RATING;
	}
}
