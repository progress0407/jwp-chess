package chess.domain.piece;

import static chess.domain.board.position.File.C;
import static chess.domain.board.position.Rank.THREE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.constant.TargetType;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class KingTest {

    @ParameterizedTest
    @CsvSource(value = {"B:THREE", "B:FOUR", "D:THREE", "B:THREE"}, delimiter = ':')
    @DisplayName("킹은 좌우, 대각선 방향으로 한칸만 이동이 가능하다")
    void isMovable(File file, Rank rank) {
        //given
        King king = new King(PieceTeam.WHITE);

        //when
        boolean actual = king.isMovable(Position.of(C, THREE), Position.of(file, rank), TargetType.EMPTY);

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"B:FIVE", "B:ONE", "C:ONE"}, delimiter = ':')
    @DisplayName("킹은 좌우, 대각선 방향으로 한 칸 이동이 아니라면 이동이 불가하다")
    void cantMovable(File file, Rank rank) {
        //given
        King king = new King(PieceTeam.WHITE);

        //when
        boolean actual = king.isMovable(Position.of(C, THREE), Position.of(file, rank), TargetType.EMPTY);

        //then
        assertThat(actual).isFalse();
    }
}
