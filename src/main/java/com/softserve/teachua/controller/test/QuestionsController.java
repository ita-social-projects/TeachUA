package com.softserve.teachua.controller.test;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.challenge.SuccessUpdatedChallenge;
import com.softserve.teachua.dto.challenge.UpdateChallenge;
import com.softserve.teachua.dto.test.question.ImportProfile;
import com.softserve.teachua.dto.test.question.QuestionPreview;
import com.softserve.teachua.dto.test.question.QuestionResponse;
import com.softserve.teachua.security.UserPrincipal;
import com.softserve.teachua.service.test.QuestionService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionsController implements Api {

    private final QuestionService questionService;

    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/questions-import")
    public void importQuestions(@RequestBody ImportProfile importProfile, Authentication authentication)
        throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        questionService.questionsImport(importProfile.getFormId(), userPrincipal.getId());
    }

    @AllowedRoles({RoleData.ADMIN, RoleData.MANAGER})
    @GetMapping("/quiz/questions")
    public List<QuestionPreview> getAllQuestions() {
        return questionService.getAllQuestions();
    }

//    @AllowedRoles(RoleData.ADMIN)
//    @PutMapping("/quiz/question/{id}")
//    public SuccessUpdatedQuestion updateQuestion(@PathVariable Long id,
//                                                   @Valid @RequestBody UpdateChallenge updateChallenge) {
//        return questionService.updateChallenge(id, updateChallenge);
//    }

}
