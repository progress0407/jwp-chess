package chess.dao;

import chess.domain.db.Game;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameDao {

    private static final String IS_EXIST_GAME_DML = "select exists (select game_id from games limit 1 ) as `exists`";
    private static final String FIND_LAST_GAME_DML = "select * from games order by create_at desc limit 1";
    private static final String SAVE_DML = "insert into games (game_id, last_team, create_at) values (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public boolean isExistGame() {
        return jdbcTemplate.queryForObject(IS_EXIST_GAME_DML, Boolean.class);
    }

    public void save(String gameId, String lastTeamName) {
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update(SAVE_DML, gameId, lastTeamName, createdAt);
    }

    public Game findLastGame() {
        return jdbcTemplate.queryForObject(FIND_LAST_GAME_DML,
                (rs, rowNum) -> new Game(
                        rs.getString("game_id"),
                        rs.getString("last_team"),
                        rs.getTimestamp("create_at")
                )
        );
    }
}
