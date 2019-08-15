# ActorMaterializerの使い方とメモリリークの関係

## 動作確認
※gatlingでどう負荷を掛けるかは`com.github.BambooTuna.MemoryLeakInvestigation.gatling.test`
以下を編集してください。

```bash
//terminal.1
$ sbt boot/run

//terminal.2
$ sbt clean gatling:test
```