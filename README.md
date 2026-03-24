### 概要
コンパスが最も近くにいるタグを持つプレイヤーを指すようになります

### 利用方法
1. コンパスにタグを追加します
   ```bash
   /targettag <name>
   ```
2. プレイヤーに同名のタグを追加します
   ```bash
   /tag @s add <name>
   ```

### 利用例
以下のケースを考えます。
画像のchore_3はtag1とtag2を持っています。fix_3はtag1のみを持っています。
<img width="520" height="305" alt="image" src="https://github.com/user-attachments/assets/84dd2955-5445-4320-8393-5e64c9d6f06f" />

chore_3が近づくと、tag1とtag2のコンパスがchore_3に向きます。
<img width="520" height="307" alt="image" src="https://github.com/user-attachments/assets/12db86f9-bfeb-48e6-a381-92fa16fc178c" />

chore_3が離れ、fix_3が近づくと、tag1のコンパスのみがfix_3に向き、tag2のコンパスはchore_3に向きます。
<img width="520" height="306" alt="image" src="https://github.com/user-attachments/assets/2b724f96-f0c6-42b9-9c69-9e9a1146a3a8" />
