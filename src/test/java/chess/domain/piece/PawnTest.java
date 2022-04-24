package chess.domain.piece;

import static chess.domain.board.position.File.*;
import static chess.domain.board.position.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;

import chess.constant.TargetType;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PawnTest {

    @ParameterizedTest
    @CsvSource(value = {"THREE:true", "FOUR:true", "FIVE:false"}, delimiter = ':')
    @DisplayName("폰은 처음에 한 칸 혹은 두 칸 이동 가능하다, 그리고 세번 이동하는 것은 불가능 하다")
    void pawn_when_first_moving_can_go_one_or_two_point_moving(Rank rank, boolean expected) {
        Pawn pawn = new Pawn(PieceTeam.WHITE);
        Position source = Position.of(A, TWO);
        Position target = Position.of(A, rank);
        boolean actual = pawn.isMovable(source, target, TargetType.EMPTY);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("폰은 처음 초기 위치가 두 칸 이동하는 것이 불가하다")
    void pawn_first_move_then_cant_move_as_two_point_moving() {
        Pawn pawn = new Pawn(PieceTeam.WHITE);
        Position source = Position.of(A, FOUR);
        Position target = Position.of(A, SIX);
        boolean actual = pawn.isMovable(source, target, TargetType.EMPTY);
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("폰은 뒤로 이동하는 것이 불가하다: 흰색")
    void pawn_cant_move_backward_white() {
        Pawn pawn = new Pawn(PieceTeam.WHITE);
        Position source = Position.of(A, FOUR);
        Position target = Position.of(A, THREE);
        boolean actual = pawn.isMovable(source, target, TargetType.EMPTY);

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("폰은 뒤로 이동하는 것이 불가하다: 검은색")
    void pawn_cant_move_backward_black() {
        Pawn pawn = new Pawn(PieceTeam.BLACK);
        Position source = Position.of(A, FIVE);
        Position target = Position.of(A, SIX);
        boolean actual = pawn.isMovable(source, target, TargetType.EMPTY);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"B:true", "A:false"}, delimiter = ':')
    @DisplayName("폰은 대각선으로만 공격할 수 있다")
    void when_pawn_can_attack_diagonal(File file, boolean expected) {
        Pawn pawn = new Pawn(PieceTeam.WHITE);

        Position source = Position.of(A, FOUR);
        Position target = Position.of(file, FIVE);

        boolean actual = pawn.isMovable(source, target, TargetType.ENEMY);
        assertThat(actual).isEqualTo(expected);
    }
}
