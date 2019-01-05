import { IWord } from 'app/shared/model//word.model';

export const enum TestType {
    WRITTEN_MIXED,
    WRITTEN_PL,
    WRITTEN_KANA,
    WRITTEN_KANJI,
    TRUE_FALSE
}

export interface IWordsTest {
    id?: number;
    name?: string;
    type?: TestType;
    code?: string;
    testUrl?: string;
    creatorLogin?: string;
    creatorId?: number;
    words?: IWord[];
}

export class WordsTest implements IWordsTest {
    constructor(
        public id?: number,
        public name?: string,
        public type?: TestType,
        public code?: string,
        public testUrl?: string,
        public creatorLogin?: string,
        public creatorId?: number,
        public words?: IWord[]
    ) {}
}
