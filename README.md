# Bartforge

Local-first AI coding agent for analyzing, modifying and generating code.

## 🚀 Overview

Bartforge is a modular AI agent designed to work directly with your local codebase.
It supports multiple LLM providers and prioritizes privacy by default through local execution using Ollama.

Key capabilities (planned):

* code analysis and review
* file reading and modification
* command execution
* refactoring assistance
* multi-step agent workflows

---

## 🧱 Architecture

The project follows a clean, modular structure:

```
bartforge/
├── agent-api     # REST API layer (Spring Boot)
├── agent-core    # domain logic and orchestration
├── agent-llm     # LLM providers (OpenAI, Gemini, Ollama)
└── agent-tools   # file system, git, command tools
```

### Modules

| Module        | Responsibility                          |
| ------------- | --------------------------------------- |
| `agent-api`   | HTTP controllers and configuration      |
| `agent-core`  | Agent orchestration and domain models   |
| `agent-llm`   | Integration with LLM providers          |
| `agent-tools` | External actions (files, commands, git) |

---

## 🧠 LLM Providers

Supported providers:

* OpenAI (remote)
* Gemini (remote)
* Ollama (local, default for privacy)

### 🔒 Privacy-first approach

Sensitive code should be processed using:

```
OLLAMA (local)
```

Ollama runs locally (Docker) and ensures code does not leave your machine.

---

## 🐳 Running Ollama (local LLM)

### 1. Start Ollama

```bash
docker compose up -d
```

### 2. Pull model

```bash
docker exec -it bartforge-ollama ollama pull qwen2.5-coder:7b
```

### 3. Verify

```bash
curl http://localhost:11434/api/tags
```

---

## ⚙️ Configuration

### `application.yml`

```yaml
bartforge:
  llm:
    ollama:
      base-url: http://localhost:11434
      model: qwen2.5-coder:7b
      timeout-seconds: 30
```

### Docker environment

If running inside Docker network:

```yaml
base-url: http://ollama:11434
```

---

## 📡 API

### Chat endpoint

```
POST /api/agent/chat
```

### Request

```json
{
  "prompt": "Explain this code",
  "provider": "OLLAMA",
  "repositoryPath": "/path/to/project"
}
```

### Response

```json
{
  "provider": "OLLAMA",
  "answer": "..."
}
```

---

## 🔧 Development

### Requirements

* Java 21
* Kotlin 2.x
* Docker
* Gradle

---

### Run application

```bash
./gradlew bootRun
```

---

## 🧪 Testing Ollama directly

```bash
curl -X POST http://localhost:11434/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "qwen2.5-coder:7b",
    "messages": [
      { "role": "user", "content": "Hello" }
    ],
    "stream": false
  }'
```

---

## ⚠️ Known limitations (current stage)

* no conversation memory
* no tool execution yet
* no streaming support
* basic error handling
* single prompt → single response

---

## 🛣️ Roadmap

Next steps:

* [ ] error handling & resiliency
* [ ] tool abstraction (file, git, command)
* [ ] agent loop (multi-step reasoning)
* [ ] conversation memory
* [ ] streaming responses
* [ ] IDE integration

---

## 🧠 Design principles

* local-first (privacy by default)
* simple, extensible architecture
* minimal abstractions
* production-oriented code
* provider-agnostic LLM layer

---

## 📌 Notes

* Prefer Ollama for sensitive data
* Avoid sending code to external APIs unless required
* Keep architecture modular and testable

---

## 🧑‍💻 Author

Bartforge is a personal project focused on building a real-world AI coding agent.

---
