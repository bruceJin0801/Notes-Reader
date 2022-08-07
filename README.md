# Notes
> A PDF reader that allows a user to read and annotate a document on an Android tablet

<h2>1. Demo:</h2>

![demo](https://user-images.githubusercontent.com/85118325/183269650-bfd4e6e5-2b9c-4fe3-8dc4-d3dd8dcf7f92.gif)

<h2>2. Techinique:</h2>

* This app is implemented in IntelliJ 2022, with the Android API 30 SDK.

<h2>3. Features:</h2>

* <ins>**Load:**</ins> The application will load a sample PDF which includes any number of pages. 

* <ins>**Save:**</ins>  The changes will be saved when the user changes pages, or exits.

* <ins>**Titlebar:**</ins> The titlebar should show the name of the PDF. 

* <ins>**Statusbar:**</ins> The statusbar should show the current page number and the total number of pages.

* <ins>**Navigate:**</ins> Browse forward and backwards through the pages in the document. The status bar can be updated to indicate the current page.

* <ins>**Drawing:**</ins> The users can draw on the current page, allowing them to write notes or draw on a page.

* <ins>**Highlighting:**</ins> The user can draw over the existing document with a thick, transparent yellow brush that allows the user to highlight the text in the PDF. The highlighter is transparent enough that the text beneath it remains visible! See the diagram above for an example.
  
* <ins>**Erase:**</ins> The users are able to erase an existing drawing or highlighting.

* <ins>**Zoom & Pan:**</ins> The user can use two fingers to zoom-in and zoom-out. 
  > When zoomed-in, users can pan around to reposition the document. These gestures should behave the same as standard pan-and-zoom. Users can draw and highlight a document at any scale, and the annotations should scale with the document canvas.

* <ins>**Undo/Redo:**</ins> Undo the last change that was made to the document. The users are able to undo at least the last 5 actions that were performed. Redo can be used to revert the undo.

* <ins>**Orientation changes:**</ins> The app remains usable in either portrait or landscape mode. Rotating the device will not result in data loss. When you change orientation, the document is scaled to the width of the screen. 

