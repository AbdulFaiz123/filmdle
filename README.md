# 🎬 Filmdle

A daily Bollywood movie guessing game. Like Wordle, but you guess the movie of the day.

- One secret movie per day, the same for everyone, changing at midnight India time.
- 6 guesses. Each wrong guess unlocks a new clue and shows comparison tiles.
- 133 well-known Hindi films from 2000 to 2025. Practice mode never repeats a film until you have seen them all.
- Stats, streaks, a share button for WhatsApp, a practice mode, and English / हिंदी.
- Funny filmi sound effects (made in code, with a mute button) and TV-remote controls.

It is just two files, with no build step and no server code:

| File          | What it is                                         |
| ------------- | -------------------------------------------------- |
| `index.html`  | The whole game: page layout, styles and JavaScript |
| `movies.json` | The list of movies and all their clues             |

## Play it on your computer

The game loads `movies.json` with `fetch()`. Browsers block that when you open
a file straight from disk (`file://...`), so start a tiny local web server in
this folder instead:

```bash
python3 -m http.server 8000
```

Then open <http://localhost:8000> in your browser.

## Add a new movie

Open `movies.json` and add a new entry inside the `[ ... ]` list. Put a comma
after the entry before it. Every field is required:

```json
{
  "id": "swades",
  "title": "Swades",
  "altTitles": ["Swades: We, the People"],
  "year": 2004,
  "director": "Ashutosh Gowariker",
  "leadActors": ["Shah Rukh Khan", "Gayatri Joshi"],
  "genres": ["Drama"],
  "emojiPlot": [
    ["🧑‍🚀", "a NASA scientist"],
    ["🇮🇳", "returns to India to find his old nanny"],
    ["🏡", "he stays on in her village"],
    ["💡", "and helps it make its own electricity"]
  ],
  "song": "Yeh Jo Desh Hai Tera",
  "funFact": "Shah Rukh Khan plays a NASA scientist who returns to an Indian village and helps it build its own power supply."
}
```

| Field        | Rules                                                                                     |
| ------------ | ----------------------------------------------------------------------------------------- |
| `id`         | Unique, lowercase, dashes only. Never change it later (saved games use it).               |
| `title`      | The main title, as shown in the game.                                                     |
| `altTitles`  | Other spellings or short forms people might type (`"DDLJ"`). Can be `[]`.                 |
| `year`       | Release year as a number, no quotes.                                                      |
| `director`   | Spell it **exactly** like other movies by the same director, or the ✅ tile won't match.  |
| `leadActors` | Most important actor first (the first two become the initials clue). Same spelling rule. |
| `genres`     | One or more of: Action, Adventure, Biography, Comedy, Crime, Drama, Family, Fantasy, Historical, Horror, Musical, Mystery, Romance, Satire, Sci-Fi, Sports, Spy, Thriller, War. |
| `emojiPlot`  | 3–5 `["emoji", "what it means"]` pairs that tell the story in order. Players see only the emoji; the meanings are shown after the game. |
| `song`       | The **title** of one famous song. Never lyrics.                                           |
| `funFact`    | One sentence, written in your own words.                                                  |

To use a new genre, also add its Hindi name to `GENRE_HI` in `index.html`.

**Content rules:** no posters, stills or other images from the films, no
dialogue quotes, no song lyrics. Only song titles, and facts written by us.

**Check before you publish:** paste the file into <https://jsonlint.com> to catch
a missing comma, and double-check the year, director and cast.

**Good to know:** the daily order is a shuffle of the whole list, so adding
movies reshuffles which movie falls on which day. Publish new movies just
after midnight India time, so nobody's game changes halfway through their day.

## Play on a Fire TV

1. On the Fire TV, open the **Silk Browser** (install it from the Appstore if it's missing).
2. Type the game's address, for example `abdulfaiz123.github.io/filmdle`, and bookmark it.
3. In Silk the remote moves an **on-screen pointer** and **OK** clicks. No typing needed:
   the **Choose from a list** picker opens by itself on a Fire TV. Click a letter,
   then click a movie **twice** (once to choose it, once to guess).

Silk never sends arrow-key presses to web pages (only pointer moves and clicks), so
the arrow-key navigation in the game only works on laptops, or inside a TV app that
shows the page in a WebView.

On big screens the game shows two columns (clues on the left, guesses on the
right), and on a 1080p TV browser everything is zoomed up so it can be read from the sofa.

## Sounds

All sounds are made by code with the Web Audio API (section 11 of the script),
so there are no audio files and no film music. A wrong guess plays a dramatic
TV-serial "dhin dhin dhiiin", a win plays a dhol beat and a happy tune, and a loss
plays a sad trombone. The 🔊 button in the header turns them off.

## Publish on GitHub Pages (free)

1. Create a new **public** repository on <https://github.com/new>, for example `filmdle`.
2. Upload `index.html`, `movies.json` and `README.md`: on the repository page click
   **Add file → Upload files**, drag them in, and click **Commit changes**.
   Or from this folder in a terminal:

   ```bash
   git init && git add . && git commit -m "Filmdle" && git branch -M main
   ```

   ```bash
   git remote add origin https://github.com/YOUR-USERNAME/filmdle.git && git push -u origin main
   ```

3. In the repository go to **Settings → Pages**. Under *Build and deployment*
   choose **Deploy from a branch**, branch **main**, folder **/ (root)**, then **Save**.
4. Wait a minute or two. Your game is live at
   `https://YOUR-USERNAME.github.io/filmdle/`.

To update it later, edit the files and upload or push them again. GitHub Pages
redeploys by itself.

## How it works (short version)

- **Same movie for everyone, no server:** the game counts days since launch in
  India time. It shuffles the movie list with a *seeded* random generator, so
  everyone's browser produces the same shuffle, and day N gets movie N of that
  shuffle. After all the movies, it moves on to a new shuffle, so nothing repeats
  until the whole list has been played.
- **Saving:** your progress for today, your stats and your language are stored
  in your browser's `localStorage`. Nothing is sent anywhere.
- **Change the launch date:** `LAUNCH_DAY` near the top of the `<script>` in
  `index.html` sets which day is Filmdle #1.
