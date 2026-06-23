"use client";

import { useEffect, useState } from "react";
import { Document } from "../types/document";
import {
  getDocuments,
  deleteDocument,
  downloadDocument,
} from "../services/document.service";

export function useDocuments() {
  const [documents, setDocuments] =
    useState<Document[]>([]);

  const [loading, setLoading] =
    useState(true);

  const loadDocuments = async () => {
    try {
      const data = await getDocuments();
      setDocuments(data);
    } finally {
      setLoading(false);
    }
  };

  const remove = async (id: number) => {
    await deleteDocument(id);
    await loadDocuments();
  };

  useEffect(() => {
    loadDocuments();
  }, []);

  return {
    documents,
    loading,
    refresh: loadDocuments,
    deleteDocument: remove,
    downloadDocument,
  };
}