"use client";

import { useSubmitApplication } from "../hooks/useSubmitApplication";

type Props = {
    readonly applicationId: string;
    readonly disabled?: boolean;
};

export function SubmitApplicationButton({
    applicationId,
    disabled,
}: Props) {
    const mutation = useSubmitApplication();

    return (
        <button
            className="rounded bg-blue-600 px-3 py-2 text-white disabled:opacity-50"
            disabled={disabled || mutation.isPending}
            onClick={() => mutation.mutate(applicationId)}
        >
            {mutation.isPending
                ? "Envoi..."
                : "Déposer la demande"}
        </button>
    );
}