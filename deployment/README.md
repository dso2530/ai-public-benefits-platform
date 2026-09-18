# Helm + Argo CD deployment bundle

This bundle provides a generic Helm chart and Argo CD App-of-Apps manifests for the AI Public Benefits Platform microservices.

## Microservices covered

- citizen-bff — 8084
- document-service — 8085
- security-service — 8080 (adjust if the service exposes another port)
- document-extraction-service — 9085
- rag-service — 9087
- profile-service — 8080
- application-service — 8080
- eligibility-service — 8080
- notification-service — 8080
- consent-service — 8080
- connector-service — 8080

Ports are container/service ports and can be changed per values file without modifying the chart.

## Important assumptions

1. Images are published as `ghcr.io/dso2530/<service>:<tag>`.
2. The Git repository is `https://github.com/dso2530/ai-public-benefits-platform.git`.
3. Argo CD runs in namespace `argocd`.
4. Applications run in namespace `public-benefits`.
5. Kubernetes service names for infrastructure dependencies are represented by values such as `kafka`, `apicurio`, `paddleocr`, `clamav`, `ollama`, and `pgvector`; change these to the actual Kubernetes Services used by the infrastructure charts.
6. Several non-core service ports are set to `8080` because their exact ports were not specified in the available project context; adjust those values if the Spring Boot services expose different ports.
7. Secrets are intentionally not committed. Create Kubernetes Secrets separately and reference them with `existingSecret` or `envFrom`.
8. The ServiceMonitor is disabled by default because it requires the Prometheus Operator CRD. Enable it once that CRD is installed.
9. The Ingress is enabled only for the BFF in the dev example. Change the host and ingress class to match the cluster.

## Install

Apply the AppProject and root application:

```bash
kubectl apply -f deploy/argocd/project.yaml
kubectl apply -f deploy/argocd/root.yaml
```

The root Application discovers the child Application manifests under `deploy/argocd/applications/`.

## Validate Helm locally

```bash
helm lint deploy/helm/microservice
helm template document-service deploy/helm/microservice \
  -f deploy/helm/microservice/values/dev/common.yaml \
  -f deploy/helm/microservice/values/dev/document-service.yaml
```

## Production recommendation

Keep environment-specific values in separate files/directories, e.g. `values/staging/` and `values/prod/`, and point Argo CD Applications at the appropriate files. Do not put database passwords, OAuth client secrets, MinIO credentials, Kafka credentials, or other sensitive material in Git. Use External Secrets, Sealed Secrets, or another approved secret-management mechanism.
