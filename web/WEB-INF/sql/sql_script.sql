DROP TABLE DIRECTED_BY, DEVELOPPED_BY, DVDS, VIDEOGAMES,DEVELOPERS, MOVIE_PEOPLE,BOOKS,USERS;
CREATE TABLE USERS (
    ID_USER INT PRIMARY KEY AUTO_INCREMENT,
    EMAIL VARCHAR(200) UNIQUE NOT NULL,
    PASSWORD VARCHAR(120) NOT NULL,
    CITY VARCHAR(256) NOT NULL
    );

CREATE TABLE DVDS
(
    ID_DVD INT PRIMARY KEY AUTO_INCREMENT,
    ID_USER INT,
    TITLE VARCHAR(256),
    TYPE VARCHAR(256),
    DESCRIPTION VARCHAR(256),
    EDITOR VARCHAR(256),
    AUDIO VARCHAR(256),
    RELEASEDATE VARCHAR(256),
    AGELIMIT INT,
    DURATION INT,
    CONSTRAINT FK_USER FOREIGN KEY(ID_USER) REFERENCES USERS (ID_USER)

);

CREATE TABLE DVD_COMMENTS(
    ID_DVD_COMMENTED INT,
    ID_COMMENTER_DVD INT,
    COMMENT_CONTENT VARCHAR(10000),
    CONSTRAINT FK_DVD_COMM FOREIGN KEY (ID_DVD_COMMENTED) REFERENCES DVDS (ID_DVD),
    CONSTRAINT FK_COMMENTER_DVD FOREIGN KEY (ID_COMMENTER_DVD) REFERENCES  USERS (ID_USER),
    CONSTRAINT DVD_COMMENTS_PK PRIMARY KEY (ID_DVD_COMMENTED,ID_COMMENTER_DVD)
);

CREATE TABLE DVD_RATINGS(
            ID_DVD_RATED INT,
            ID_USER_RATING_DVD INT UNIQUE NOT NULL ,
            COMMENT_CONTENT VARCHAR(10000),
            CONSTRAINT FK_DVD_RATED FOREIGN KEY (ID_DVD_RATED) REFERENCES DVDS (ID_DVD),
            CONSTRAINT FK_USER_RATING_DVD FOREIGN KEY (ID_USER_RATING_DVD) REFERENCES  USERS (ID_USER),
            CONSTRAINT DVD_COMMENTS_PK PRIMARY KEY (ID_DVD_RATED,ID_USER_RATING_DVD)

);
CREATE TABLE MOVIE_PEOPLE(
    ID_MOVIE_PEOPLE INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(256)
                         );

CREATE TABLE DIRECTED_BY(
    MOVIE_ID INT,
    MOVIE_PERSON_ID INT,
    CONSTRAINT DIRECTED_BY_PK PRIMARY KEY(MOVIE_ID,MOVIE_PERSON_ID),
    CONSTRAINT FK_MOVIE FOREIGN KEY (MOVIE_ID) REFERENCES DVDS (ID_DVD),
    CONSTRAINT FK_MOVIE_PEOPLE FOREIGN KEY (MOVIE_PERSON_ID) REFERENCES MOVIE_PEOPLE (ID_MOVIE_PEOPLE)
);

CREATE TABLE VIDEOGAMES
(
    ID_VIDEO_GAME INT PRIMARY KEY AUTO_INCREMENT,
    ID_USERS INT,
    NAME VARCHAR(256),
    TYPE VARCHAR(256),
    RESUME VARCHAR(256),
    VIDEO_GAME_EDITOR VARCHAR(256),
    RELEASEDATE VARCHAR(256),
    CONSTRAINT FK_USER_VG FOREIGN KEY(ID_USERS) REFERENCES USERS (ID_USER)
);

CREATE TABLE DEVELOPERS(
    ID_DEVELOPER INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(256),
    LASTNAME VARCHAR(256)
);

CREATE TABLE DEVELOPPED_BY(
        VIDEO_GAME_ID INT,
        DEVELOPER_ID INT,
        CONSTRAINT DEVELOPED_BY_PK PRIMARY KEY(VIDEO_GAME_ID,DEVELOPER_ID),
        CONSTRAINT FK_VIDEO_GAME FOREIGN KEY (VIDEO_GAME_ID) REFERENCES VIDEOGAMES (ID_VIDEO_GAME),
        CONSTRAINT FK_DEVELOPER FOREIGN KEY (DEVELOPER_ID) REFERENCES DEVELOPERS (ID_DEVELOPER)
);

CREATE TABLE VG_COMMENTS(
        ID_VG_COMMENTED INT,
        ID_COMMENTER_VG INT,
        COMMENT_CONTENT VARCHAR(10000),
        CONSTRAINT FK_VG_COMM FOREIGN KEY (ID_VG_COMMENTED) REFERENCES VIDEOGAMES (ID_VIDEO_GAME),
        CONSTRAINT FK_COMMENTER_VG FOREIGN KEY (ID_COMMENTER_VG) REFERENCES  USERS (ID_USER),
        CONSTRAINT VG_COMMENTS_PK PRIMARY KEY (ID_COMMENTER_VG,ID_VG_COMMENTED)
);

CREATE TABLE VG_RATINGS(
            ID_VG_RATED INT,
            ID_USER_RATING INT UNIQUE NOT NULL,
            RATING_VG INT,
            CONSTRAINT FK_VG_RATED FOREIGN KEY (ID_VG_RATED) REFERENCES VIDEOGAMES (ID_VIDEO_GAME),
            CONSTRAINT FK_USER_RATING_VG FOREIGN KEY (ID_USER_RATING) REFERENCES  USERS (ID_USER),
            CONSTRAINT VG_COMMENTS_PK PRIMARY KEY (ID_VG_RATED,ID_USER_RATING)
);

CREATE TABLE BOOKS
(
    ID_BOOK INT PRIMARY KEY AUTO_INCREMENT,
    ID_USERS INT,
    AUTHOR VARCHAR(256),
    TITLE VARCHAR(256),
    TYPE VARCHAR(256),
    DESCRIPTION VARCHAR(256),
    RELEASEDATE VARCHAR(256),
    EDITOR VARCHAR(256),
    PLATEFORM VARCHAR(256),
    AGELIMIT INT,
    CONSTRAINT FK_USER_BOOKS FOREIGN KEY(ID_USERS) REFERENCES USERS (ID_USER)
);

CREATE TABLE BOOK_COMMENTS(
    ID_BOOK_COMMENTED INT,
    ID_COMMENTER INT,
    COMMENT_CONTENT VARCHAR(10000),
    CONSTRAINT FK_BOOK_COMM FOREIGN KEY (ID_BOOK_COMMENTED) REFERENCES BOOKS (ID_BOOK),
    CONSTRAINT FK_COMMENTER_BOOK FOREIGN KEY (ID_COMMENTER) REFERENCES  USERS (ID_USER),
    CONSTRAINT BOOK_COMMENTS_PK PRIMARY KEY (ID_COMMENTER,ID_BOOK_COMMENTED)
);

CREATE TABLE BOOK_RATING(
    ID_BOOK_RATED INT,
    ID_USER_RATING INT UNIQUE NOT NULL,
    RATING INT,
    CONSTRAINT FK_BOOK_RATED FOREIGN KEY (ID_BOOK_RATED) REFERENCES BOOKS(ID_BOOK),
    CONSTRAINT FK_USER_RATING_BOOK FOREIGN KEY (ID_USER_RATING) REFERENCES  USERS (ID_USER),
    CONSTRAINT BOOK_COMMENTS_PK PRIMARY KEY (ID_BOOK_RATED,ID_USER_RATING)
)