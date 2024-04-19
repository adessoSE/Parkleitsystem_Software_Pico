package de.adesso.softwarepico.messageHandling.error;

import de.adesso.communication.messageHandling.error.DidNotRespondException;
import de.adesso.communication.messageHandling.MessageErrorHandler;
import de.adesso.softwarepico.service.mirror.MirrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoftwarePicoErrorHandler implements MessageErrorHandler {

    private final MirrorService mirrorService;

    @Autowired
    public SoftwarePicoErrorHandler(MirrorService mirrorService) {
        this.mirrorService = mirrorService;
    }

    @Override
    public boolean supports(Exception e) {
        return e instanceof DidNotRespondException;
    }

    @Override
    public void handle(Exception e) {
        if(supports(e)){
            DidNotRespondException d = (DidNotRespondException) e;
            if(d.getExpected().getJsonMessage().has("important") && d.getExpected().getJsonMessage().getInt("important") == 1) {
                mirrorService.setConnectionStatus("LOST");
            }
        }
    }
}
