import { useEffect, useState } from "react";
import { getBenefits, Eligibility } from "../services/benefit.service";
export function useBenefits() {
  const [benefits, setBenefits] = useState<Eligibility[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getBenefits()
      .then(setBenefits)
      .finally(() => setLoading(false));
  }, []);

  return { benefits, loading };
}