package chess.controller.view;

import chess.service.ChessService;
import chess.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static chess.util.RandomCreationUtils.createUuid;
import static chess.util.StringUtils.isEmpty;

@Controller
@RequiredArgsConstructor
public class ChessViewController {

    private final ChessService chessService;



    @GetMapping("/")
    public String root(Model model, @RequestParam(required = false) Boolean isEmptyRoomInfo) {

        if (isEmptyRoomInfo != null) {
            model.addAttribute("isEmptyRoomInfo", isEmptyRoomInfo);
        }

        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    @PostMapping("/games/first")
    public String playNewGame(@RequestParam("room-name") String roomName,
                              @RequestParam("room-password") String roomPassword,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (isEmpty(roomName) || isEmpty(roomPassword)) {
            redirectAttributes.addAttribute("isEmptyRoomInfo", true);
            return "redirect:/";
        }

        model.addAttribute("isNewGame", true);
        model.addAttribute("createdRoomId", createUuid());
        model.addAttribute("roomName", roomName);
        model.addAttribute("encryptedRoomPassword", CryptoUtils.encrypt(roomPassword));

        return "game";
    }

    @GetMapping("/load-game")
    public String loadGame(HttpSession session, Model model) {

        if (!chessService.isExistGame()) {
            return "redirect:/";
        }

        model.addAttribute("isNewGame", false);

        return "game";
    }


}
