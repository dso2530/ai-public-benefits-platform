"use client";

import { useState } from "react";
import { uploadDocument } from "../services/document.service";

export function useUploadDocument(
  onSuccess?: () => Promise<void>
) {
  const [loading, setLoading] = useState(false);

  const upload = async (
    file: File,
    type: string
  ) => {
    try {
      setLoading(true);

      await uploadDocument(file, type);

      if (onSuccess) {
        await onSuccess();
      }
    } finally {
      setLoading(false);
    }
  };

  return {
    upload,
    loading,
  };
}