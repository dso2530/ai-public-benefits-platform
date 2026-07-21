import { useMutation, useQueryClient } from "@tanstack/react-query";
import { submitApplication } from "../services/application.service";

export function useSubmitApplication() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: submitApplication,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["applications"],
            });
        },
    });
}