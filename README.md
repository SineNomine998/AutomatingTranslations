# Automating Translations

During an internship, I had to create translations for a new language. Since there was a lot to translate, I wrote this script to automate the process. It's written in Java, because I needed the script ASAP and I already had some similar experience and reference code in Java. This script is specifically written for `.po` translation files in Django projects. All lines are stored in a LinkedList and after translations are done, written in a new file with a specified name along with the correct translations.

## API
Create an .env file to store the `API_KEY` and `API_HOST` for the external API that is used to translate text from English to `target` language. 
