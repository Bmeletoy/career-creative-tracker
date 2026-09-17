**A few deliberate design calls:**
- **No stored "overdue" flag.** `ProjectStatusTracker.isOverdue()` computes it at query time — a stored derived value is a second source of truth waiting to disagree with reality.
- **`Project → Stage` is one-directional.** `Stage` holds the foreign key; `Project` has no back-reference collection. Keeps lazy-loading and serialization simple, and makes plain-JUnit testing painless.
- **A `Project` is "done" only when every required `Stage` is done** (`optional` stages are excluded from that check) — not just the last stage by order, since nothing enforces stages completing in sequence.
- **`ApplicationDetails` uniqueness is enforced at the application layer** (`findByProjectId().isEmpty()` + `DuplicateApplicationDetailsException` → `409`) — DB-level `@Column(unique = true)` is a planned hardening pass, not yet in place.

---

## Testing strategy

Built in three deliberate layers, each proving a different thing:

| Layer | Tool | Proves | Status |
|---|---|---|---|
| **Unit** | Plain JUnit 5 | Pure logic (`isDone()`, `isOverdue()`) with zero external dependencies | Complete |
| **Repository** | `@DataJpaTest` + embedded H2 | The JPA mappings and queries actually persist and query correctly | Complete |
| **Controller** | `@SpringBootTest` + MockMvc + `@MockitoBean` | The full HTTP round trip — routing, JSON serialization, and `@ControllerAdvice` error handling all work end-to-end | In progress |

Every layer is run and verified as a **full passing batch** (not spot-checked test-by-test) before being considered done — raw JUnit protocol output, not "looked fine to me."

---

## Project status

| # | Milestone | Status |
|---|---|---|
| 0 | Schema design + environment setup | Done |
| 1 | Entities | Done |
| 2 | Repositories (JPA + Postgres) | Done |
| 3 | Controllers — REST endpoints | Done |
| 3.5 | Global exception handling | Done |
| 4 | Testing — unit → repository → controller | In progress |
| 5 | Docker + CI (GitHub Actions) | ⬜ Planned |
| 6 | React frontend + analytics dashboard | ⬜ Planned |
| 7 | AWS deployment | ⬜ Planned |

---

## Built to learn, not to copy-paste

This project is being built end-to-end by hand as a deliberate learning exercise. Every line of application code, every test, and every design decision is mine.

---

## Running it locally

```bash
# clone
git clone https://github.com/Bmeletoy/career-creative-tracker.git
cd career-creative-tracker/backend

# make sure PostgreSQL is running with a `tracker` database available
brew services start postgresql@16

# run
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. Run the test suite with:

```bash
./mvnw test
```

---

## Roadmap

- [ ] Finish `@SpringBootTest`/MockMvc coverage for all three controllers
- [ ] Dockerize the backend (multi-stage build)
- [ ] GitHub Actions CI — run the full test suite on every push
- [ ] React + Recharts dashboard (`/api/stats`: response rate by résumé variant, referral vs. cold conversion)
- [ ] Deploy to AWS (EC2 + RDS or Elastic Beanstalk)
- [ ] JWT auth + input validation hardening
- [ ] Second vertical: creative-project tracking (`domain = CREATIVE`)

---

*Built by [Bolu Meletoyitan](https://github.com/Bmeletoy) — UMD CS '24.*
