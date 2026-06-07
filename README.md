# AI Public Benefits Optimization Platform

> Discover, qualify, and automate access to public benefits that citizens never claim.

## Overview

The AI Public Benefits Optimization Platform is a cloud-native GovTech solution designed to reduce the "non-take-up" of public benefits by helping citizens identify, qualify for, and apply to national, regional, departmental, and municipal aid programs.

The platform combines:

- AI-powered document processing
- Knowledge Graph technology
- Event-driven microservices
- Eligibility engines
- Public administration connectors
- Agentic AI workflows

The goal is simple:

> Upload your tax notice and instantly discover all public benefits available to your household.

---

# Business Problem

Every year, billions of euros of public assistance remain unclaimed because:

- Citizens are unaware of available programs
- Eligibility criteria are difficult to understand
- Local aid programs are fragmented
- Administrative procedures are complex
- Forms require repetitive data entry

This platform automates the entire process.

---

# Key Features

## Citizen Identity Management

- FranceConnect Integration
- Consent Management
- Secure Authentication
- Household Identity Resolution

---

## Document Intelligence

Supported documents:

- Tax notices
- Rental receipts
- Utility bills
- CAF certificates
- School certificates
- Energy consumption reports

Capabilities:

- OCR extraction
- Document classification
- Structured data extraction
- Automatic validation

---

## Household Knowledge Graph

Builds a unified representation of:

- Citizens
- Families
- Households
- Properties
- Documents
- Public benefits

Example:

```json
{
  "householdId": "HH001",
  "taxReferenceIncome": 21800,
  "children": 2,
  "singleParent": true,
  "city": "Roubaix",
  "housingStatus": "TENANT"
}
```

---

## Eligibility Engine

Determines:

- Eligible Benefits
- Potentially Eligible Benefits
- Missing Documentation
- Confidence Score

Supports:

- National programs
- Regional programs
- Department programs
- Municipal programs

---

## Territorial Intelligence Platform

Daily monitoring of:

- Municipal websites
- Regional portals
- Department councils
- Government agencies
- Public decrees

Automatically detects:

- New grants
- New eligibility criteria
- Funding updates
- Benefit expiration dates

---

## AI Application Assistant

Automates:

- Form generation
- Document collection
- Application packaging
- Submission workflow
- Renewal reminders

---

# High-Level Architecture

```text
Frontend (Next.js)
          |
          v
Kong API Gateway
          |
          v
Spring Boot Microservices
          |
          v
Apache Kafka Event Backbone
          |
          +----------------+
          |                |
          v                v
      PostgreSQL       Neo4j
                          |
                          v
                Household Knowledge Graph
                          |
                          v
                 AI & Eligibility Services
                          |
                          v
                    Vertex AI
                    Document AI
```

---

# Technology Stack

## Frontend

| Component | Technology |
|------------|------------|
| Framework | Next.js |
| Language | TypeScript |
| UI | TailwindCSS |
| Components | Shadcn/UI |
| State Management | Zustand |
| Data Fetching | TanStack Query |
| Forms | React Hook Form |
| Validation | Zod |

---

## Backend

| Component | Technology |
|------------|------------|
| Language | Java 24+ |
| Framework | Spring Boot 4 |
| Security | Spring Security |
| API | REST |
| Documentation | OpenAPI |
| Event Streaming | Kafka |
| Serialization | Avro |

---

## AI Layer

| Component | Technology |
|------------|------------|
| LLM Integration | Spring AI |
| OCR | Google Document AI |
| AI Models | Vertex AI |
| Embeddings | Vertex AI |
| Vector Search | Qdrant |
| Knowledge Graph | Neo4j |

---

## Infrastructure

| Component | Technology |
|------------|------------|
| Cloud Provider | Google Cloud Platform |
| Kubernetes | GKE Autopilot |
| API Gateway | Kong |
| Object Storage | Google Cloud Storage |
| Monitoring | Prometheus |
| Dashboards | Grafana |
| Tracing | OpenTelemetry |
| GitOps | ArgoCD |
| IaC | Terraform |

---

# Repository Structure

```text
ai-public-benefits-platform/
│
├── frontend/
│
├── backend/
│   ├── core-services/
│   ├── connectors/
│   ├── graph/
│   └── gateway/
│
├── ai-services/
│
├── shared/
│
├── contracts/
│
├── infra/
│
├── deployment/
│
└── docs/
```

---

# Microservices

## Core Services

```text
identity-service
profile-service
document-service
eligibility-service
notification-service
consent-service
```

---

## Connectors

```text
franceconnect-connector
dgfip-connector
caf-connector
msa-connector
france-travail-connector
linky-connector
```

---

## AI Services

```text
ocr-service
extraction-service
territorial-rag-service
benefit-matching-service
ai-copilot-service
```

---

# Event-Driven Architecture

Kafka Topics:

```text
citizen.authenticated

document.uploaded

ocr.completed

document.extracted

profile.updated

eligibility.requested

eligibility.completed

benefit.detected

application.generated

notification.sent
```

---

# Security

The platform handles highly sensitive citizen data.

Security measures include:

- FranceConnect authentication
- OAuth2 / OIDC
- PKCE
- JWT authentication
- End-to-end encryption
- Data encryption at rest
- Audit logging
- GDPR compliance
- Consent tracking
- Secrets Manager integration

---

# Deployment Environments

```text
local
staging
production
```

Deployment strategy:

```text
GitHub Actions
      |
      v
Artifact Registry
      |
      v
ArgoCD
      |
      v
GKE Autopilot
```

---

# MVP Scope

The initial MVP focuses on:

### Phase 1

- FranceConnect login
- Tax notice upload
- OCR processing
- Household profile generation
- Eligibility calculation
- Benefits dashboard

### Phase 2

- Regional benefit catalog
- AI explanations
- Application pre-filling
- Notification center

### Phase 3

- Automated submission
- Continuous monitoring
- AI Copilot
- Multi-region support

---

# Example User Journey

```text
User Login
    |
    v
FranceConnect Authentication
    |
    v
Upload Tax Notice
    |
    v
OCR Extraction
    |
    v
Profile Construction
    |
    v
Eligibility Matching
    |
    v
Benefits Dashboard
    |
    v
Application Generation
    |
    v
Submission Tracking
```

---

# Long-Term Vision

Create the first AI-powered platform capable of:

- Mapping every public benefit available in France
- Building a real-time eligibility engine
- Automatically generating applications
- Reducing administrative complexity
- Increasing household purchasing power

The platform aims to become the digital co-pilot for public benefit optimization.

---

## License

Proprietary - All Rights Reserved

Copyright © 2026
