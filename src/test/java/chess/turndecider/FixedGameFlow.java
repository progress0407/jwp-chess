package chess.turndecider;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.turndecider.state.State;
import chess.turndecider.state.WhiteTeam;

public class FixedGameFlow implements GameFlow {

    private final State currentState = new WhiteTeam();

    public boolean isCorrectTurn(Piece sourcePiece) {
        return true;
    }

    @Override
    public void nextState(boolean isFinished) {
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public State currentState() {
        return currentState;
    }

    @Override
    public PieceTeam currentPieceTeam() {
        return currentState.pieceTeam();
    }
}

