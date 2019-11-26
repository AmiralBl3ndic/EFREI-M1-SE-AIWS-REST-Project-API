package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.DVD;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;

@NoArgsConstructor @AllArgsConstructor
public class MovieDAOImpl implements DAO<DVD> {




	@Override
	public void create(@NonNull DVD obj) {

	}

	@Override
	public void update(@NonNull DVD obj) {

	}

	@Override
	public void delete(@NonNull DVD obj) {

	}

	@Override
	public DVD findBy(String db) throws SQLException {
		return null;
	}
}
