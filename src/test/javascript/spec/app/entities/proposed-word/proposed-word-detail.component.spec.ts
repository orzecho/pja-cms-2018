/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { ProposedWordDetailComponent } from 'app/entities/proposed-word/proposed-word-detail.component';
import { ProposedWord } from 'app/shared/model/proposed-word.model';

describe('Component Tests', () => {
    describe('ProposedWord Management Detail Component', () => {
        let comp: ProposedWordDetailComponent;
        let fixture: ComponentFixture<ProposedWordDetailComponent>;
        const route = ({ data: of({ proposedWord: new ProposedWord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ProposedWordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProposedWordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProposedWordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.proposedWord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
