import { IWord } from 'app/shared/model/word.model';

export interface IVocabularyTestItem {
    srcTranslationLanguage?: string;
    translationFromSystem?: string;
    kanaFromSystem?: string;
    kanjiFromSystem?: string;
    translationFromUser?: string;
    kanaFromUser?: string;
    kanjiFromUser?: string;
    translationCorrect?: boolean;
    kanaCorrect?: boolean;
    kanjiCorrect?: boolean;
    word?: IWord;
}

export class VocabularyTestItem implements IVocabularyTestItem {
    constructor(
        public srcTranslationLanguage?: string,
        public translationFromSystem?: string,
        public kanaFromSystem?: string,
        public kanjiFromSystem?: string,
        public translationFromUser?: string,
        public kanaFromUser?: string,
        public kanjiFromUser?: string,
        public translationCorrect?: boolean,
        public kanaCorrect?: boolean,
        public kanjiCorrect?: boolean,
        public word?: IWord
    ) {}
}
