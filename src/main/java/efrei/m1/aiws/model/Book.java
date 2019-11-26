package efrei.m1.aiws.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @NoArgsConstructor
public class Book
{
	private static final int DEFAULT_RATING = 0;
	private static final int MINIMUM_RATING = 0;
	private static final int MAXIMUM_RATING = 5;

	private String bookId;

	private String userId;

	private String author;

	private String title;

	private String type;

	private String description;

	private String releaseDate;

	private String editor;

	private int ageLimit;

	@Setter(AccessLevel.NONE)
	private int rating;

	public void setRating(int rating) {
		if (rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING) {
			this.rating = rating;
		}
	}

	public Book(int userId, int bookId, String author, String title, String type, String description, String releaseDate, String editor, int ageLimit, int rating) {
		this.userId = userId;
		this.bookId = bookId;
		this.author = author;
		this.title = title;
		this.type = type;
		this.description = description;
		this.releaseDate = releaseDate;
		this.editor = editor;
		this.ageLimit = ageLimit;
		this.rating = rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING ? rating : DEFAULT_RATING;
	}
}
