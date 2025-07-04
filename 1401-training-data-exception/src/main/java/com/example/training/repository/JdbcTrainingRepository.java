package com.example.training.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.training.entity.Training;
import com.example.training.exception.TrainingIdAlreadyExistsException;

@Repository
public class JdbcTrainingRepository implements TrainingRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcTrainingRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Training selectById(String id) {
		return jdbcTemplate.queryForObject("SELECT * FROM training WHERE id=?",
				new DataClassRowMapper<>(Training.class),
				id);
	}

    @Override
    public List<Training> selectAll() {
        return jdbcTemplate.query("SELECT * FROM training", new DataClassRowMapper<>(Training.class));
    }

    @Override
    public boolean update(Training training) {
        int count = jdbcTemplate.update("UPDATE training SET title=?, start_date_time=?, end_date_time=?, reserved=?, capacity=? WHERE id=?",
                training.getTitle(),
                training.getStartDateTime(),
                training.getEndDateTime(),
                training.getReserved(),
                training.getCapacity(),
                training.getId());
        return count > 0;
    }

    @Override
    public void insert(Training training) {
    	try {
    		jdbcTemplate.update("INSERT INTO training VALUES (?,?,?,?,?,?)",
    				training.getId(),
    				training.getTitle(),
    				training.getStartDateTime(),
    				training.getEndDateTime(),
    				training.getReserved(),
    				training.getCapacity());
    	} catch (DuplicateKeyException e) {
    		// キーが重複した場合の処理
    		System.err.println("研修ID '" + training.getId() + "' は既に存在します。");
    		// 例外を再スローするか、特定のビジネス例外に変換してスローすることもできます。
    		throw new TrainingIdAlreadyExistsException("指定された研修IDは既に存在します。", e);
    		// または null を返してコントローラーで処理
    		// return null;
    	} catch (DataAccessException e) {
    		// その他のデータアクセスエラー
    		System.err.println("データベース操作中にエラーが発生しました: " + e.getMessage());
    		throw e; // または適切な例外に変換
    	}
    }

    @Override
    public boolean delete(String id) {
        int count = jdbcTemplate.update("DELETE FROM training WHERE id=?", id);
        return count > 0;
    }

}
