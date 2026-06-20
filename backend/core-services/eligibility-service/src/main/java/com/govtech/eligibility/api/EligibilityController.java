
package  com.govtech.eligibility.api;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.govtech.eligibility.application.usecase.GetEligibilitySummaryService;
import com.govtech.eligibility.application.dto.EligibilityDto;
import com.govtech.eligibility.application.dto.EligibilitySummaryDto;
import com.govtech.eligibility.application.usecase.GetEligibilityListService;

@RestController
@RequiredArgsConstructor
public class EligibilityController {

    private final GetEligibilitySummaryService summaryService;
    private final GetEligibilityListService listService;

    @GetMapping("/api/eligibility/summary")
    public EligibilitySummaryDto summary() {
        return summaryService.execute();
    }

    @GetMapping("/api/eligibility")
    public List<EligibilityDto> eligibilities() {
        return listService.execute();
    }
}