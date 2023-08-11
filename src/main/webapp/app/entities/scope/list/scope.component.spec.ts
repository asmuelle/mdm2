import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ScopeService } from '../service/scope.service';

import { ScopeComponent } from './scope.component';

describe('Scope Management Component', () => {
  let comp: ScopeComponent;
  let fixture: ComponentFixture<ScopeComponent>;
  let service: ScopeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'scope', component: ScopeComponent }]), HttpClientTestingModule, ScopeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ScopeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScopeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ScopeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.scopes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to scopeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getScopeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getScopeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
