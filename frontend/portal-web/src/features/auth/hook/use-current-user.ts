// features/auth/hooks/use-current-user.ts

import { useQuery }
from "@tanstack/react-query";

import { authService }
from "../services/auth.service";

export const useCurrentUser = () => {

  return useQuery({

    queryKey: ["me"],

    queryFn: authService.getCurrentUser,

    retry: false
  });
};