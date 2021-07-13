# shadow-cljs + quil quickstart

This is a minimum template you can use as the basis for quil projects.

![image](https://user-images.githubusercontent.com/5009316/125375320-3495d400-e3cc-11eb-8162-ba812c1a70d7.png)

## Required Software

- [node.js (v6.0.0+)](https://nodejs.org/en/download/)
- [Java JDK (8+)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or [Open JDK (8+)](http://jdk.java.net/10/)

## Running the Example

```bash
git clone https://github.com/Saikyun/shadow-cljs-quil-starter.git quickstart
cd quickstart
npm install
npx shadow-cljs server
```

This runs the `shadow-cljs` server process which all following commands will talk to. Just leave it running and open a new terminal to continue.

The first startup takes a bit of time since it has to download all the dependencies and do some prep work. Once this is running we can get started.

```txt
npx shadow-cljs watch app
```

This will begin the compilation of the configured `:app` build and re-compile whenever you change a file.

When you see a "Build completed." message your build is ready to be used.

```txt
[:app] Build completed. (23 files, 4 compiled, 0 warnings, 7.41s)
```

You can now then open [http://localhost:8020](http://localhost:8020).

You should see a spiral, rendered using quil. If you modify `src/starter/quil.cljs` your changes should be reflected in the browser.

## Useful links

- [shadow-cljs docs](https://shadow-cljs.github.io/docs/UsersGuide.html)
- [quil examples & docs](http://quil.info/)
