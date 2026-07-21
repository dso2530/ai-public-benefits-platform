CREATE INDEX idx_eligibility_subject
ON eligibilities(subject);

CREATE INDEX idx_eligibility_calculation
ON eligibilities(calculation_id);

CREATE INDEX idx_eligibility_subject_calculated_at
ON eligibilities(subject, calculated_at DESC);