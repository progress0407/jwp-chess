package chess.domain.board;

import static chess.domain.board.position.File.A;
import static chess.domain.board.position.File.B;
import static chess.domain.board.position.File.C;
import static chess.domain.board.position.Rank.EIGHT;
import static chess.domain.board.position.Rank.FOUR;
import static chess.domain.board.position.Rank.ONE;
import static chess.domain.board.position.Rank.SEVEN;
import static chess.domain.board.position.Rank.THREE;
import static chess.domain.board.position.Rank.TWO;
import static chess.domain.piece.PieceTeam.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.board.factory.StringBoardFactory;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import chess.domain.gameflow.AlternatingGameFlow;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.exception.NonMovableException;
import chess.exception.PieceNotAtStartingPositionException;
import chess.gameflow.FixedGameFlow;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChessBoardTest {

    private ChessBoard chessBoard;
    FixedGameFlow gameFlow;
    private final BoardFactory boardFactory = RegularBoardFactory.getInstance();

    @BeforeEach
    void setUp() {
        gameFlow = new FixedGameFlow();
        chessBoard = new ChessBoard(boardFactory, gameFlow);
    }

    @Test
    @DisplayName("체스 보드 생성 테스트 : 개수")
    void init_count() {
        //given
        Map<Position, Piece> piecesByPositions = chessBoard.getBoard();

        //when
        int actual = piecesByPositions.keySet().size();

        //then
        assertThat(actual).isEqualTo(64);
    }

    @Test
    @DisplayName("체스 보드 생성 테스트 : 폰이 있는 행")
    void init_pawns_only() {
        //given
        Map<Position, Piece> piecesByPositions = chessBoard.getBoard();

        // when & then
        List<Piece> expected = Arrays.stream(File.values())
            .map(value -> new Pawn(WHITE))
            .collect(Collectors.toList());

        List<Piece> actual = Arrays.stream(File.values())
            .map(file -> piecesByPositions.get(Position.of(file, TWO)))
            .collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스 보드 생성 테스트 : 폰이 있는 행")
    void init_Except_Pawn() {
        //given
        Map<Position, Piece> piecesByPositions = chessBoard.getBoard();

        //when
        List<Piece> actual = Arrays.stream(File.values())
                .map(file -> piecesByPositions.get(Position.of(file, TWO)))
                .collect(Collectors.toList());

        List<Piece> expected = Arrays.stream(File.values())
            .map(value -> new Pawn(WHITE))
            .collect(Collectors.toList());

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스 말이 없는 곳에서 이동 시키면 예외를 던진다.")
    void move_exception() {
        assertThatThrownBy(() -> chessBoard.movePiece(Position.of(A, THREE), Position.of(B, THREE)))
            .isInstanceOf(PieceNotAtStartingPositionException.class);
    }

    @Test
    @DisplayName("체스 말이 입력한 target으로 정상 이동했는지 확인한다.")
    void move_test() {
        //when
        chessBoard.movePiece(Position.of(A, TWO), Position.of(A, THREE));
        Map<Position, Piece> piecesByPositions = chessBoard.getBoard();

        //then
        assertThat(piecesByPositions.get(Position.of(A, THREE))).isEqualTo(new Pawn(WHITE));
    }

    @ParameterizedTest
    @CsvSource(value = {"ONE:A", "THREE:C"}, delimiter = ':')
    @DisplayName("퀸은 경로에 다른 기물 있으면 이동할 수 없다")
    void isBlocked(Rank rank, File file) {
        assertThatThrownBy(() ->
            chessBoard.movePiece(Position.of(C, ONE), Position.of(file, rank))
        ).isInstanceOf(NonMovableException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"THREE:C", "THREE:A"}, delimiter = ':')
    @DisplayName("나이트는 경로에 다른 기물 있으면 이동할 수 있다")
    void isNonBlocked(Rank rank, File file) {
        assertDoesNotThrow(() ->
            chessBoard.movePiece(Position.of(B, ONE), Position.of(file, rank))
        );
    }

    @DisplayName("기물이 다른 기물의 이동경로를 막고 있다면 이동이 불가하다")
    @Test
    void isBlockedAfterNightMoved() {
        chessBoard.movePiece(Position.of(B, ONE), Position.of(C, THREE));
        assertThatThrownBy(() ->
            chessBoard.movePiece(Position.of(C, TWO), Position.of(C, FOUR))
        ).isInstanceOf(NonMovableException.class);
    }

    @DisplayName("이동 하는 곳에 아군 기물이 있으면 이동이 불가능 하다")
    @Test
    void isMyTeam() {
        assertThatThrownBy(() ->
            chessBoard.movePiece(Position.of(A, ONE), Position.of(A, TWO))
        ).isInstanceOf(NonMovableException.class);
    }

    @DisplayName("폰을 A2 에서 A4로 이동시켰다면 A4에는 폰이 있다")
    @Test
    void move_pawn_and_now_pawn_is_at_target_pos() {
        Position from = Position.of(A, TWO);
        Position to = Position.of(A, FOUR);
        chessBoard.movePiece(from, to);
        Piece findPiece = chessBoard.getBoard().get(Position.of(A, FOUR));
        assertThat(findPiece).isInstanceOf(Pawn.class);
    }

    @DisplayName("킹이 잡힐 경우 게임은 종료된다")
    @Test
    void when_king_captured_then_game_end() {
        List<String> stringChessBoard = List.of(
                "K.......",
                ".p......",
                "........",
                "........",
                "........",
                "........",
                "........",
                "........"
        );

        BoardFactory boardFactory = StringBoardFactory.getInstance(stringChessBoard);
        AlternatingGameFlow gameFlow = new AlternatingGameFlow();
        chessBoard = new ChessBoard(boardFactory, gameFlow);

        assertThat(chessBoard.isGamePlaying()).isTrue();

        chessBoard.movePiece(Position.of(B, SEVEN), Position.of(A, EIGHT)); // 흰 폰 이동

        assertThat(chessBoard.isGamePlaying()).isFalse();
    }

    @Test
    @DisplayName("첫판에 점수를 계산하면 38점이 나온다")
    void when_first_turn_cal_score_then_38() {
        ChessBoard chessBoard = new ChessBoard(boardFactory, new AlternatingGameFlow());
        double score = chessBoard.calculateScoreByGameFlow();
        assertThat(score).isEqualTo(38.0);
    }

    @Test
    @DisplayName("점수 계산 - 초기 한 쪽 팀의 점수는 38점이다")
    void score_is_38_when_first_time() {
        ChessBoard chessBoard = new ChessBoard(boardFactory, new AlternatingGameFlow());

        //then
        double actual = chessBoard.calculateScoreByGameFlow();
        assertThat(actual).isEqualTo(38.0);
    }

    @Test
    @DisplayName("폰이 같은 File(열) 에 두 개 이상 있을 경우 각 0.5점으로 계산한다.")
    void when_pawns_in_same_file() {
        List<String> stringChessBoard = List.of(
                "........",
                "........",
                "........",
                "........",
                "........",
                "..p.....",
                ".pp.....",
                "ppp....."
        );
        BoardFactory boardFactory = StringBoardFactory.getInstance(stringChessBoard);
        ChessBoard chessBoard = new ChessBoard(boardFactory, new AlternatingGameFlow());

        //then
        double actual = chessBoard.calculateScoreByGameFlow();
        assertThat(actual).isEqualTo(3.5);
    }

    @Test
    @DisplayName("흰색팀과 검은색팀의 스코어를 검색한다")
    void calc_score_white_and_black() {
        List<String> stringChessBoard = List.of(
                "RNBQK...",
                ".....PPP",
                "........",
                "........",
                "........",
                "p.......",
                "p.......",
                "...qk..."
        );
        BoardFactory boardFactory = StringBoardFactory.getInstance(stringChessBoard);
        ChessBoard chessBoard = new ChessBoard(boardFactory, new AlternatingGameFlow());

        //then
        double blackScore = chessBoard.calculateScoreByTeam(PieceTeam.BLACK);
        double whiteScore = chessBoard.calculateScoreByTeam(WHITE);

        assertThat(blackScore).isEqualTo(22.5);
        assertThat(whiteScore).isEqualTo(10.0);
    }
}
