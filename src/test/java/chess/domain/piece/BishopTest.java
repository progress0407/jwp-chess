package chess.domain.piece;

import static chess.domain.board.position.File.*;
import static chess.domain.board.position.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;

import chess.constant.TargetType;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BishopTest {

    @DisplayName("비숍은 오로지 대각선 방향으로만 이동이 가능하다")
    @ParameterizedTest
    @CsvSource(value = {"D:FOUR", "E:FIVE", "B:FOUR", "B:TWO"}, delimiter = ':')
    void isMovable(File file, Rank rank) {
        //given
        Bishop bishop = new Bishop(PieceTeam.WHITE);
        Position source = Position.of(C, THREE);
        Position target = Position.of(file, rank);

        //when
        boolean actual = bishop.isMovable(source, target, TargetType.EMPTY);

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"D:THREE", "E:FOUR"}, delimiter = ':')
    @DisplayName("비숍은 대각선이 아니면 이동이 불가능하다")
    void cantMovable(File file, Rank rank) {
        //given
        Bishop bishop = new Bishop(PieceTeam.WHITE);
        Position source = Position.of(C, THREE);
        Position target = Position.of(file, rank);

        //when
        boolean actual = bishop.isMovable(source, target, TargetType.EMPTY);

        //then
        assertThat(actual).isFalse();
    }
}
