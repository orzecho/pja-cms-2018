import { ITag } from 'app/shared/model//tag.model';

export interface IWord {
    id?: number;
    translation?: string;
    kana?: string;
    kanji?: string;
    romaji?: string;
    note?: string;
    tags?: ITag[];
    rawTags?: string;
}

export class Word implements IWord {
    constructor(
        public id?: number,
        public translation?: string,
        public kana?: string,
        public kanji?: string,
        public romaji?: string,
        public note?: string,
        public tags?: ITag[],
        public rawTags?: string
    ) {}
}
