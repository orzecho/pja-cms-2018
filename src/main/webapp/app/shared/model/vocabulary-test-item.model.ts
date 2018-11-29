export interface IVocabularyTestItem {
    translationFromSystem?: string;
    kanaFromSystem?: string;
    kanjiFromSystem?: string;
    translationFromUser?: string;
    kanaFromUser?: string;
    kanjiFromUser?: string;
    translationCorrect?: boolean;
    kanaCorrect?: boolean;
    kanjiCorrect?: boolean;
}

export class VocabularyTestItem implements IVocabularyTestItem {
    constructor(
        public translationFromSystem?: string,
        public kanaFromSystem?: string,
        public kanjiFromSystem?: string,
        public translationFromUser?: string,
        public kanaFromUser?: string,
        public kanjiFromUser?: string,
        public translationCorrect?: boolean,
        public kanaCorrect?: boolean,
        public kanjiCorrect?: boolean
    ) {}
}
