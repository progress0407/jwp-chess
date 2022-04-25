package chess.domain.piece;

import static chess.domain.board.position.MoveDirection.of;

import chess.constant.TargetType;
import chess.domain.board.position.MoveDirection;
import chess.domain.board.position.Position;
import java.util.List;
import java.util.Objects;

public abstract class Piece {

    final PieceTeam pieceTeam;

    public Piece(PieceTeam pieceTeam) {
        this.pieceTeam = pieceTeam;
    }

    public abstract boolean isMovable(Position source, Position target, TargetType targetType);

    public abstract double getScore();

    public abstract String getConcreteEmblem();

    public boolean isMyTeam(Piece other) {
        return this.pieceTeam == other.pieceTeam;
    }

    public boolean isSameColor(PieceTeam otherPieceTeam) {
        return this.pieceTeam == otherPieceTeam;
    }

    public boolean isKing() {
        return this instanceof King;
    }

    public boolean isPawn() {
        return this instanceof Pawn;
    }

    public boolean isKnight() {
        return this instanceof Knight;
    }

    public String getEmblem() {
        if (pieceTeam == PieceTeam.WHITE) {
            return getConcreteEmblem().toLowerCase();
        }
        return getConcreteEmblem();
    }

    boolean isMovable(Position from, Position to, TargetType targetType, List<MoveDirection> possibleMoveDirections) {
        MoveDirection moveDirection = of(from, to);

        if (moveDirection.isNothing()) {
            return false;
        }

        return possibleMoveDirections.contains(moveDirection);
    }

    public boolean isPieceTeam(PieceTeam pieceTeam) {
        return this.pieceTeam == pieceTeam;
    }

    public String teamName() {
        return pieceTeam.name();
    }

    public String className() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return pieceTeam == piece.pieceTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceTeam);
    }

    @Override
    public String toString() {
        return "Piece{" +
                "pieceTeam=" + pieceTeam +
                '}';
    }
}
