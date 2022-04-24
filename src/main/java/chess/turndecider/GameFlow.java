package chess.turndecider;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.turndecider.state.State;

public interface GameFlow {

    boolean isCorrectTurn(Piece sourcePiece);

    void nextState(boolean isGameFinished);

    boolean isRunning();

    State currentState();
    PieceTeam currentPieceTeam();
}
