package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.BoardPieceDao;
import chess.dao.GameDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.DbBoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.board.position.Position;
import chess.domain.db.BoardPiece;
import chess.domain.db.Game;
import chess.domain.gameflow.AlternatingGameFlow;
import chess.domain.gameflow.GameFlow;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import chess.repository.SessionToChessRepository;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final GameDao gameDao;
    private final BoardPieceDao boardPieceDao;
    private final SessionToChessRepository sessionToChessRepository;

    public ChessBoard initAndGetChessBoard(HttpSession session) {
        ChessBoard chessBoard = createChessBoard();

        sessionToChessRepository.add(session, chessBoard);
        return chessBoard;
    }

    private ChessBoard createChessBoard() {
        BoardFactory boardFactory = RegularBoardFactory.getInstance();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory.create(), gameFlow);
    }

    public ChessBoard getChessBoard(HttpSession session) {
        return sessionToChessRepository.get(session);
    }

    public void movePiece(HttpSession session,
                          Position from,
                          Position to) {
        ChessBoard chessBoard = sessionToChessRepository.get(session);
        chessBoard.movePiece(from, to);
    }

    @Transactional(readOnly = true)
    public boolean isExistGame() {
        return gameDao.isExistGame();
    }

    @Transactional
    public void saveGame(SaveRequest saveRequest) {
        String gameId = createUuid();
        gameDao.save(gameId, saveRequest.getCurrentTeam(), saveRequest.getCreatedAt());
        boardPieceDao.save(gameId, saveRequest.getPieces());
    }

    @Transactional(readOnly = true)
    public GameResponse loadLastGame(HttpSession session) {
        Game lastGame = gameDao.findLastGame();
        String lastGameId = lastGame.getGameId();
        String lastTeam = lastGame.getLastTeam();
        List<BoardPiece> lastBoardPieces = boardPieceDao.findLastBoardPiece(lastGameId);
        BoardFactory boardFactory = new DbBoardFactory(lastBoardPieces);
        ChessBoard chessBoard = new ChessBoard(boardFactory, lastTeam);
        sessionToChessRepository.add(session, chessBoard);
        return new GameResponse(chessBoard);
    }

    @Transactional
    public void clearAll() {
        gameDao.deleteAll();
    }
}
