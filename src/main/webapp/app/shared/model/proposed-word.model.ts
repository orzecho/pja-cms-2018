import { ITag } from 'app/shared/model//tag.model';

export interface IProposedWord {
    id?: number;
    translation?: string;
    kana?: string;
    kanji?: string;
    romaji?: string;
    note?: string;
    tags?: ITag[];
    addedByLogin?: string;
    addedById?: number;
}

export class ProposedWord implements IProposedWord {
    constructor(
        public id?: number,
        public translation?: string,
        public kana?: string,
        public kanji?: string,
        public romaji?: string,
        public note?: string,
        public tags?: ITag[],
        public addedByLogin?: string,
        public addedById?: number
    ) {}
}
