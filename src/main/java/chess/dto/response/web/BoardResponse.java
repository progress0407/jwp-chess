package chess.dto.response.web;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardResponse {

    private List<Map<String, String>> board;

    public BoardResponse(List<Map<String, String>> board) {
        this.board = board;
    }

    public static BoardResponse from(Map<Position, Piece> board) {

        final List<Map<String, String>> boardString = new ArrayList<>();

        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            final Map<String, String> keyValue = getPiecePositionResponse(position, piece);

            boardString.add(keyValue);
        }

        return new BoardResponse(boardString);
    }

    private static Map<String, String> getPiecePositionResponse(Position position, Piece piece) {
        final Map<String, String> keyValue = new HashMap<>();

        keyValue.put("position", PositionResponse.from(position));
        keyValue.put("piece", piece.getClass().getSimpleName());
        keyValue.put("team", piece.teamName().toLowerCase());

        return keyValue;
    }

    public List<Map<String, String>> getBoard() {
        return board;
    }
}
