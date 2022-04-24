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

public class RookTest {

    @ParameterizedTest
    @CsvSource(value = {"A:THREE", "H:THREE", "C:ONE", "C:EIGHT"}, delimiter = ':')
    @DisplayName("Rook은 동일 Rank나 File에 있는 좌표로 이동할 수 있다")
    void isMovable(File file, Rank rank) {
        //given
        Rook rook = new Rook(PieceTeam.WHITE);

        //when
        boolean actual = rook.isMovable(Position.of(C, THREE), Position.of(file, rank), TargetType.EMPTY);

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"A:ONE", "B:TWO", "D:FOUR"}, delimiter = ':')
    @DisplayName("Rook은 Rank 나 File 모두 동일하지 않다면 이동이 불가하다 (false 반환)")
    void cantMovable(File file, Rank rank) {
        //given
        Rook rook = new Rook(PieceTeam.WHITE);

        //when
        boolean actual = rook.isMovable(Position.of(C, THREE), Position.of(file, rank), TargetType.EMPTY);

        //then
        assertThat(actual).isFalse();
    }
}
