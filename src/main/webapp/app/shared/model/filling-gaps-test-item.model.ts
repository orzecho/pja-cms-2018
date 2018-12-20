import { IGapItem } from 'app/shared/model//gap-item.model';
import { ITag } from 'app/shared/model//tag.model';

export interface IFillingGapsTestItem {
    id?: number;
    question?: string;
    gapItems?: IGapItem[];
    tags?: ITag[];
    rawTags?: string[];
}

export class FillingGapsTestItem implements IFillingGapsTestItem {
    constructor(
        public id?: number,
        public question?: string,
        public gapItems?: IGapItem[],
        public tags?: ITag[],
        public rawTags?: string[]
    ) {}
}
